    package br.ufes.dwws.cantosparamissa.core.application;

    import br.ufes.dwws.cantosparamissa.core.domain.User;
    import br.ufes.inf.labes.jbutler.ejb.application.CrudService;

    import java.util.List;

    public interface ManageUserService extends CrudService<User> {
        List<User> findByEmailContaining(String emailPart);
        void createUser(User user, String password);
        void updatePassword(User user, String newPassword);
        List<User> findAll();
    }
