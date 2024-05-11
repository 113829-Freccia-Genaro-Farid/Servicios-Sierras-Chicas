import {Roles} from "./roles";

export interface Usuario {
  email:string;
  activo:boolean;
  fechaAlta:Date;
  rol:Roles;
}
