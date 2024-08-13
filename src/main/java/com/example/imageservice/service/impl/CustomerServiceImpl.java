package com.example.imageservice.service.impl;

import com.example.imageservice.entity.Customer;
import com.example.imageservice.entity.Image;
import com.example.imageservice.entity.User;
import com.example.imageservice.model.request.SearchReq;
import com.example.imageservice.query.CustomRsqlVisitor;
import com.example.imageservice.repository.BaseRepository;
import com.example.imageservice.repository.CustomerRepository;
import com.example.imageservice.service.CustomerService;
import com.example.imageservice.service.MinIOService;
import com.example.imageservice.service.UserService;
import com.example.imageservice.util.ContextUtil;
import com.example.imageservice.util.DateUtil;
import com.example.imageservice.util.ObjectMapperUtils;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class CustomerServiceImpl extends BaseServiceImpl<Customer> implements CustomerService {
    private static final String DELETED_FILTER = ";isActive>-1";

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ContextUtil contextUtil;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Autowired
    private MinioClient minioClient;

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

    @Override
    public Customer create(Customer t) throws Exception {
        t.setStatus(0);
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

    @Override
    public Customer getById(Long id) throws Exception {
        Customer result = this.getRepository().findById(id).orElseThrow(
                () -> new Exception(String.format("Dữ liệu có id %s không tồn tại!", id))
        );
        getAllFileWithName(result.getImages());
        return result;
    }

    public List<Image> getAllFileWithName(List<Image> req) {
        req.stream().forEach(e -> {
            try {
                String presignedObjectUrl = minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(e.getUrl()).build()
                );
                e.setUrl(presignedObjectUrl);
            } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException ex) {
                System.err.println("Error occurred: " + ex);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        return req;
    }
}
