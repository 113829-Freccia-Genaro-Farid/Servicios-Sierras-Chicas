import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Profesionista} from "../../models/profesionista";
import {MensajeRespuesta} from "../../models/mensaje-respuesta";
import {Anuncio} from "../../models/anuncio";
import {Resenia} from "../../models/resenia";
import {ReseniaDTO} from "../../DTOs/resenia-dto";
import {ReseniaStats} from "../../models/resenia-stats";
import {ProfesionistaDTOPut} from "../../DTOs/profesionista-dtoput";
import {ProfesionistaDTOPost} from "../../DTOs/profesionista-dtopost";

@Injectable({
  providedIn: 'root'
})
export class ProfesionistasService {

  constructor(private client:HttpClient) { }

  getProfesionistaById(idProfesionista:number): Observable<Profesionista> {
    return this.client.get<Profesionista>("http://localhost:8080/api/profesionistas/"+idProfesionista);
  }
  getProfesionistaByUserEmail(userEmail:string): Observable<Profesionista> {
    return this.client.get<Profesionista>("http://localhost:8080/api/profesionistas/byUserEmail/"+userEmail);
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
  putPresentacionProfesionista(idProfesionista:number, descripcion:string): Observable<MensajeRespuesta> {
    return this.client.put<MensajeRespuesta>("http://localhost:8080/api/profesionistas/presentacion/"+ idProfesionista,descripcion);
  }
  putSuscripcionProfesionista(idProfesionista:number): Observable<MensajeRespuesta> {
    return this.client.put<MensajeRespuesta>("http://localhost:8080/api/profesionistas/suscripcion/"+ idProfesionista,{});
  }
  putProfesionista(idProfesionista:number, profesionistaDTOPut:ProfesionistaDTOPut): Observable<MensajeRespuesta> {
    return this.client.put<MensajeRespuesta>("http://localhost:8080/api/profesionistas/"+ idProfesionista, profesionistaDTOPut);
  }
  postProfesionista(profesionistaDTOPost:ProfesionistaDTOPost): Observable<MensajeRespuesta> {
    return this.client.post<MensajeRespuesta>("http://localhost:8080/api/profesionistas", profesionistaDTOPost);
  }

  /* ANUNCIOS */

  postClickAnuncio(idProfesionista:number): Observable<MensajeRespuesta> {
    return this.client.post<MensajeRespuesta>("http://localhost:8080/api/profesionistas/clickAnuncio/"+ idProfesionista,{});
  }

  getClicksAnuncio(idProfesionista:number): Observable<Anuncio[]> {
    return this.client.get<Anuncio[]>("http://localhost:8080/api/profesionistas/anuncios/"+idProfesionista);
  }


  /* RESEÑAS */
  getReseniasByProfesionista(idProfesionista:number): Observable<Resenia[]> {
    return this.client.get<Resenia[]>("http://localhost:8080/api/resenas/profesionista/"+idProfesionista);
  }

  getReseniasStatsByProfesionista(idProfesionista:number): Observable<ReseniaStats> {
    return this.client.get<ReseniaStats>("http://localhost:8080/api/resenas/stats/"+idProfesionista);
  }
  getPromedioReseniasByProfesionista(idProfesionista:number): Observable<number> {
    return this.client.get<number>("http://localhost:8080/api/resenas/promedio/"+idProfesionista);
  }
  postResenia(reseniaDTO:ReseniaDTO) : Observable<MensajeRespuesta> {
    return this.client.post<MensajeRespuesta>("http://localhost:8080/api/resenas",reseniaDTO);
  }
}
