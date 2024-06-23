import {Persona} from "./persona";
import {Profesion} from "./Auxiliares/profesion";

export interface Profesionista {
  id: number;
  poseeMatricula: boolean;
  nroMatricula: string;
  comunicacionWsp: boolean;
  presentacion: string;
  profesiones: string[];
  suscrito: boolean;
  persona: Persona;
  promedioResenias:number;
}
