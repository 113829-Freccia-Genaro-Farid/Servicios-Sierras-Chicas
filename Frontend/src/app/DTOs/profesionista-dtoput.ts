import {Persona} from "../models/persona";

export interface ProfesionistaDTOPut {
  poseeMatricula: boolean;
  nroMatricula: string;
  comunicacionWsp: boolean;
  idProfesiones: number[];
}
