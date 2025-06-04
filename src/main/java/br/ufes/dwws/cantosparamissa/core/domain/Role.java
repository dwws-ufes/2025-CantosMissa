package br.ufes.dwws.cantosparamissa.core.domain;

public enum Role {
  ADMIN("admin");

  private final String key;

  Role(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

}
