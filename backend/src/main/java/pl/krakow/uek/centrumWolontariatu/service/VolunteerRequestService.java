package pl.krakow.uek.centrumWolontariatu.service;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.krakow.uek.centrumWolontariatu.domain.User;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequest;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestCategory;
import pl.krakow.uek.centrumWolontariatu.domain.VolunteerRequestType;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestCategoryRepository;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestRepository;
import pl.krakow.uek.centrumWolontariatu.repository.VolunteerRequestTypeRepository;
import pl.krakow.uek.centrumWolontariatu.util.rsql.CustomRsqlVisitor;
import pl.krakow.uek.centrumWolontariatu.web.rest.AuthenticationController;
import pl.krakow.uek.centrumWolontariatu.web.rest.errors.general.BadRequestAlertException;
import pl.krakow.uek.centrumWolontariatu.web.rest.vm.VolunteerRequestVM;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VolunteerRequestService {

    private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    private final VolunteerRequestRepository volunteerRequestRepository;
    private final VolunteerRequestCategoryRepository volunteerRequestCategoryRepository;
    private final VolunteerRequestTypeRepository volunteerRequestTypeRepository;
    private final UserService userService;

    public VolunteerRequestService(
        VolunteerRequestRepository volunteerRequestRepository,
        VolunteerRequestCategoryRepository volunteerRequestCategoryRepository,
        VolunteerRequestTypeRepository volunteerRequestTypeRepository,
        UserService userService) {
        this.volunteerRequestRepository = volunteerRequestRepository;
        this.volunteerRequestCategoryRepository = volunteerRequestCategoryRepository;
        this.volunteerRequestTypeRepository = volunteerRequestTypeRepository;
        this.userService = userService;
    }

    /*
     * VOLUNTEER REQUEST
     */

    public VolunteerRequest create(VolunteerRequestVM vm) {

//        for (String categoryName : vm.getCategories()) {
//            if (!volunteerRequestCategoryRepository.findById(categoryName).isPresent()) {
//                throw new BadRequestAlertException("Unable to find category of volunteerRequest in database",
//                    "categoryManagement", "categoryNotFound");
//            }
//        }

        VolunteerRequest vr = new VolunteerRequest();
        vr.setTitle(vm.getTitle());
        vr.setDescription(vm.getDescription());
        vr.setIsForTutors((byte) (vm.isForTutors() ? 1 : 0));
        vr.setIsForStudents((byte) (vm.isForStudents() ? 1 : 0));
        vr.setVolunteersAmount(vm.getVolunteersAmount());

        return this.volunteerRequestRepository.save(vr);
    }

    public void delete(long id) {
        volunteerRequestRepository.deleteById(id);
    }

    public void changeAccepted(long id) {
        volunteerRequestRepository.findById(id).ifPresent(volunteerRequest -> {
            if (volunteerRequest.getAccepted() == 1) {
                volunteerRequest.setAccepted((byte) 0);
            } else {
                volunteerRequest.setAccepted((byte) 1);
            }

            volunteerRequestRepository.save(volunteerRequest);
        });
    }

    public Page<VolunteerRequest> getAll(Pageable pageable) {
        return this.volunteerRequestRepository.findAll(pageable);
    }

    public Page<VolunteerRequest> getAllByCurrentUser(Pageable pageable) {
        User user = userService.getUserWithAuthorities().get();
        return volunteerRequestRepository.findAllByCreatedBy(pageable, user);
    }

    public Page<VolunteerRequest> findAllByRsql(Pageable pageable, String search) {
        final Node rootNode = new RSQLParser().parse(search);
        Specification<VolunteerRequest> spec = rootNode.accept(new CustomRsqlVisitor<>());

        return volunteerRequestRepository.findAll(spec, pageable);
    }

    public Optional<VolunteerRequest> getOne(long id) {
        return this.volunteerRequestRepository.getById(id);
    }


    /*
     * CATEGORIES
     */

    public void createCategory(String name) {
        if (volunteerRequestCategoryRepository.findById(name).isPresent()) {
            throw new BadRequestAlertException("category already exist in database", "categoryManagement", "categoryExist");
        }
        VolunteerRequestCategory volunteerRequestCategory = new VolunteerRequestCategory();
        volunteerRequestCategory.setName(name);
        volunteerRequestCategoryRepository.save(volunteerRequestCategory);
    }

    public List<String> getAllCategories() {
        List<String> list = new ArrayList<>();
        for (VolunteerRequestCategory vrc : volunteerRequestCategoryRepository.findAll()) {
            list.add(vrc.getName());
        }

        return list;
    }

    public void deleteCategory(String name) {
        volunteerRequestCategoryRepository.deleteById(name);
    }


    /*
     * TYPES
     */

    public void createType(String name) {
        if (volunteerRequestTypeRepository.findById(name).isPresent()) {
            throw new BadRequestAlertException("type already exist in database", "categoryManagement", "typeExist");
        }
        VolunteerRequestType volunteerRequestType = new VolunteerRequestType();
        volunteerRequestType.setName(name);

        volunteerRequestTypeRepository.save(volunteerRequestType);
    }

    public void deleteType(String name) {
        volunteerRequestTypeRepository.deleteById(name);
    }

    public List<String> getAllTypes() {
        List<String> list = new ArrayList<>();
        for (VolunteerRequestType vrt : this.volunteerRequestTypeRepository.findAll()) {
            list.add(vrt.getName());
        }
        return list;
    }

}
