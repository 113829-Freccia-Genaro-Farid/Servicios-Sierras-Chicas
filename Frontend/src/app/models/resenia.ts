import {Profesionista} from "./profesionista";
import {Cliente} from "./cliente";

export interface Resenia {
  id: number;
  descripcion: string;
  calificacion: number;
  fechaResenia: Date;
  cliente: Cliente;
  profesionista: Profesionista;
}
