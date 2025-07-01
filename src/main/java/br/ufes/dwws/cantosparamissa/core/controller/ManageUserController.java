package br.ufes.dwws.cantosparamissa.core.controller;

import br.ufes.dwws.cantosparamissa.core.application.ManageUserService;
import br.ufes.dwws.cantosparamissa.core.domain.User;
import br.ufes.inf.labes.jbutler.ejb.application.CrudService;
import br.ufes.inf.labes.jbutler.ejb.controller.CrudController;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@PermitAll
public class ManageUserController extends CrudController<User> implements Serializable {

    @EJB
    private ManageUserService manageUserService;

    private User newUser;
    private String newUserPassword;

    private List<User> users;

    private User selectedUserForPasswordChange;
    private String newPassword;

    @PostConstruct
    public void init() {
        newUser = new User();
        users = manageUserService.findAll();
    }

    @Override
    protected CrudService<User> getCrudService() {
        return manageUserService;
    }

    public void createUser() {
        manageUserService.createUser(newUser, newUserPassword);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário criado com sucesso!"));
        newUser = new User();
        newUserPassword = "";
        users = manageUserService.findAll();
    }

    public void updatePassword() {
        manageUserService.updatePassword(selectedUserForPasswordChange, newPassword);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Senha atualizada com sucesso!"));
    }

    /*public void setSelectedUserForPasswordChange(User user) {
        this.selectedUserForPasswordChange = user;
        this.newPassword = "";
    }*/

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public String getNewUserPassword() {
        return newUserPassword;
    }

    public void setNewUserPassword(String newUserPassword) {
        this.newUserPassword = newUserPassword;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public User getSelectedUserForPasswordChange() {
        return selectedUserForPasswordChange;
    }

    public void setSelectedUserForPasswordChange(User selectedUserForPasswordChange) {
        this.selectedUserForPasswordChange = selectedUserForPasswordChange;
    }
}
