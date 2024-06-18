import {Persona} from "./persona";
import {Profesion} from "./Auxiliares/profesion";

export interface Profesionista {
  id: number;
  PoseeMatricula: boolean;
  NroMatricula: string;
  ComunicacionWsp: boolean;
  presentacion: string;
  suscrito: boolean;
  profesiones: string[];
  persona: Persona;
}
