package com.example.imageservice.service.impl;

import com.example.imageservice.constants.Status;
import com.example.imageservice.entity.BaseEntity;
import com.example.imageservice.model.request.SearchReq;
import com.example.imageservice.query.CustomRsqlVisitor;
import com.example.imageservice.repository.BaseRepository;
import com.example.imageservice.service.BaseService;
import com.example.imageservice.util.DateUtil;
import com.example.imageservice.util.ObjectMapperUtils;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    private static final String DELETED_FILTER = ";isActive>-1";

    protected abstract BaseRepository<T> getRepository();

    @Override
    public List<T> getAll() {
        return this.getRepository().findAll();
    }


    @Override
    public Page<T> search(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<T> spec = rootNode.accept(new CustomRsqlVisitor<T>());
        Pageable pageable = getPage(req);
        return this.getRepository().findAll(spec, pageable);
    }

    @Override
    public List<T> getByActive() {
        return this.getRepository().findAllByStatus(1);
    }

    protected Pageable getPage(SearchReq req) {
        String[] sortList = req.getSort().split(",");
        Sort.Direction direction = sortList[1].equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return req.getSize() != null
                ?
                PageRequest.of(req.getPage(), req.getSize(), direction, sortList[0])
                :
                Pageable.unpaged();
    }

    @Override
    public T create(T t) throws Exception {
        t.setStatus(0);
        t.setCreateDate(DateUtil.getCurrenDateTime());
        t.setIsActive(1);
        return this.getRepository().save(t);
    }

    @Override
    public T update(T t) throws Exception {
        T entityMy = this.getById(t.getId());
        ObjectMapperUtils.map(t, entityMy);
        t.setUpdateDate(DateUtil.getCurrenDateTime());
        return getRepository().save(entityMy);
    }

    @Override
    public T getById(Long id) throws Exception {
        return this.getRepository().findById(id).orElseThrow(
                () -> new Exception(String.format("Dữ liệu có id %s không tồn tại!", id))
        );
    }

    @Override
    public void delete(Long id) {
        T t = this.getRepository().findAllById(id);
        t.setIsActive(Status.DELETED);
        this.getRepository().save(t);
    }

//    @Override
//    public void createAll(List<T> entities) {
//        entities.forEach(e -> {
//            if (e.getStatus() == null) e.setStatus(Status.ACTIVE);
//        });
//        this.getRepository().saveAll(entities);
//    }
}
