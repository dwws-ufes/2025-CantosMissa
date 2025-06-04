package br.ufes.dwws.cantosparamissa.core.application;

import br.ufes.dwws.cantosparamissa.core.application.exceptions.SystemAlreadyInstalledException;

public interface ConfigureSystemService {
    void installSystem() throws SystemAlreadyInstalledException;
}
