import {Profesionista} from "./profesionista";

export interface Anuncio {
  id: number;
  cantidadClicks: number;
  fecha: Date;
  profesionista: Profesionista;
}
