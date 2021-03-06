export class Employee {
  public id: number;
  public username: string;
  public password: string;
  public firstName: string;
  public lastName: string;
  public role: Role;
}

export class Role {
  public id: number;
  public name: string;
}
