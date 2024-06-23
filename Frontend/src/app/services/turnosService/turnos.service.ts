import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Turno} from "../../models/turno";
import {TurnoDTO} from "../../DTOs/turno-dto";
import {MensajeRespuesta} from "../../models/mensaje-respuesta";

@Injectable({
  providedIn: 'root'
})
export class TurnosService {

  constructor(private client:HttpClient) { }

  getTurnos(): Observable<Turno[]> {
    return this.client.get<Turno[]>("http://localhost:8080/api/turnos");
  }
  getTurnosByProfesionista(idProfesionista:number): Observable<Turno[]> {
    return this.client.get<Turno[]>("http://localhost:8080/api/turnos/profesionista/"+idProfesionista);
  }

  getTurnosById(id:number): Observable<Turno> {
    return this.client.get<Turno>("http://localhost:8080/api/turnos/"+id);
  }

  postTurno(turno:TurnoDTO): Observable<MensajeRespuesta> {
    return this.client.post<MensajeRespuesta>("http://localhost:8080/api/turnos",turno);
  }
  putTurno(turno:TurnoDTO, idTurno:number): Observable<MensajeRespuesta> {
    return this.client.put<MensajeRespuesta>("http://localhost:8080/api/turnos/"+ idTurno,turno);
  }
  deleteTurno(idTurno:number):Observable<MensajeRespuesta>{
    return this.client.delete<MensajeRespuesta>("http://localhost:8080/api/turnos/"+ idTurno);
  }
}
