import {Cliente} from "../models/cliente";
import {Profesionista} from "../models/profesionista";

export interface TurnoDTO {
  descripcion:string;
  fechaTurno: Date;
  idCliente:number | null;
  idProfesionista:number;
}
