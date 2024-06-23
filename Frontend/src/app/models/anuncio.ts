import {Profesionista} from "./profesionista";

export interface Anuncio {
  id: number;
  cantidadClicks: number;
  anio: number;
  mes:number;
  profesionista: Profesionista;
}
