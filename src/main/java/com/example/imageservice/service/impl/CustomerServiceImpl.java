package com.example.imageservice.service.impl;

import com.example.imageservice.constants.Status;
import com.example.imageservice.entity.Customer;
import com.example.imageservice.entity.User;
import com.example.imageservice.model.request.SearchReq;
import com.example.imageservice.model.response.BaseResponse;
import com.example.imageservice.query.CustomRsqlVisitor;
import com.example.imageservice.repository.BaseRepository;
import com.example.imageservice.repository.CustomerRepository;
import com.example.imageservice.repository.ImageRepository;
import com.example.imageservice.service.CustomerService;
import com.example.imageservice.service.ImageService;
import com.example.imageservice.service.MinIOService;
import com.example.imageservice.service.UserService;
import com.example.imageservice.util.ContextUtil;
import com.example.imageservice.util.DateUtil;
import com.example.imageservice.util.ObjectMapperUtils;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer> implements CustomerService {
    private static final String DELETED_FILTER = ";isActive>-1";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserService userService;
//
//    @Autowired
//    private ImageService imageService;
//
    @Autowired
    private ContextUtil contextUtil;
//
//    @Autowired
//    private MinIOService minIOService;

    @Override
    protected BaseRepository<Customer> getRepository() {
        return customerRepository;
    }

    @Override
    public Page<Customer> search(SearchReq req) {
        req.setFilter(req.getFilter().concat(DELETED_FILTER));
        User user = userService.getUserByUsername(contextUtil.getUserName());
        if (user.getRole().equals("collaborators")){
            req.setFilter(req.getFilter().concat(";user.id==" + user.getId()));
        }
        Node rootNode = new RSQLParser().parse(req.getFilter());
        Specification<Customer> spec = rootNode.accept(new CustomRsqlVisitor<Customer>());
        Pageable pageable = getPage(req);
        return this.getRepository().findAll(spec, pageable);
    }

//    @Override
//    public BaseResponse customCreate(MultipartFile file, Customer customer) {
//        try {
//            User user = userService.getUserByUsername(contextUtil.getUserName());
//            customer.setUser(user);
//            Customer result = super.create(customer);
//            minIOService.uploadFile(file, result);
//            return new BaseResponse().success(result);
//        }catch (Exception e){
//            return new BaseResponse().fail(e.getMessage());
//        }
//    }

    @Override
    public Customer create(Customer t) throws Exception {
        t.setStatus(Status.ACTIVE);
        t.setIsActive(1);
        t.setCreateDate(DateUtil.getCurrenDateTime());
//        t.setUser(userService.getUserByUsername(contextUtil.getUserName()));
        return this.getRepository().save(t);
    }

    @Override
    public Customer update(Customer t) throws Exception {
        Customer entityMy = this.getById(t.getId());
        ObjectMapperUtils.map(t, entityMy);
        t.setUpdateDate(DateUtil.getCurrenDateTime());
        return getRepository().save(entityMy);
    }
}
