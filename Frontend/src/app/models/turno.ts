import {Cliente} from "./cliente";
import {Profesionista} from "./profesionista";

export interface Turno {
  idTurno: number;
  descripcion:string;
  fechaTurno: Date;
  cliente:Cliente;
  profesionista:Profesionista;
}
