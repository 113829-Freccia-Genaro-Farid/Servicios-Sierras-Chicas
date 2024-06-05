import {Roles} from "./Auxiliares/roles";

export interface Usuario {
  email:string;
  activo:boolean;
  fechaAlta:Date;
  rol:Roles;
}
