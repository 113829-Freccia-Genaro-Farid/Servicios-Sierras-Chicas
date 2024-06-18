import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Provincia} from "../../models/Auxiliares/provincia";
import {Profesionista} from "../../models/profesionista";
import {MensajeRespuesta} from "../../models/mensaje-respuesta";
import {Anuncio} from "../../models/anuncio";
import {Resenia} from "../../models/resenia";
import {ReseniaDTO} from "../../DTOs/resenia-dto";
import {ReseniaStats} from "../../models/resenia-stats";

@Injectable({
  providedIn: 'root'
})
export class ProfesionistasService {

  constructor(private client:HttpClient) { }

  getProfesionistaById(idProfesionista:number): Observable<Profesionista> {
    return this.client.get<Profesionista>("http://localhost:8080/api/profesionistas/"+idProfesionista);
  }
  getProfesionistas(): Observable<Profesionista[]> {
    return this.client.get<Profesionista[]>("http://localhost:8080/api/profesionistas");
  }
  getProfesionistasOrdSuscrito(): Observable<Profesionista[]> {
    return this.client.get<Profesionista[]>("http://localhost:8080/api/profesionistas/ordenada");
  }

  getProfesionistasFiltros(nombreApellido: string, categorias: number[], profesiones: number[], ciudades: number[]): Observable<Profesionista[]> {
    let params = new HttpParams();
    if (nombreApellido) {
      params = params.set('nombreApllido', nombreApellido);
    }
    if (categorias && categorias.length > 0) {
      params = params.set('categorias', categorias.join(','));
    }
    if (profesiones && profesiones.length > 0) {
      params = params.set('profesiones', profesiones.join(','));
    }
    if (ciudades && ciudades.length > 0) {
      params = params.set('ciudades', ciudades.join(','));
    }
    return this.client.get<Profesionista[]>("http://localhost:8080/api/profesionistas/filtrada", { params });
  }

  /* ANUNCIOS */

  postClickAnuncio(idProfesionista:number): Observable<MensajeRespuesta> {
    return this.client.post<MensajeRespuesta>("http://localhost:8080/api/profesionistas/clickAnuncio/"+ idProfesionista,{});
  }

  getClicksAnuncio(idProfesionista:number): Observable<Anuncio> {
    return this.client.get<Anuncio>("http://localhost:8080/api/profesionistas/anuncios/"+idProfesionista);
  }


  /* RESEÑAS */
  getReseniasByProfesionista(idProfesionista:number): Observable<Resenia[]> {
    return this.client.get<Resenia[]>("http://localhost:8080/api/resenas/profesionista/"+idProfesionista);
  }

  getReseniasStatsByProfesionista(idProfesionista:number): Observable<ReseniaStats> {
    return this.client.get<ReseniaStats>("http://localhost:8080/api/resenas/stats/"+idProfesionista);
  }

  postResenia(reseniaDTO:ReseniaDTO) : Observable<MensajeRespuesta> {
    return this.client.post<MensajeRespuesta>("http://localhost:8080/api/resenas",reseniaDTO);
  }
}
