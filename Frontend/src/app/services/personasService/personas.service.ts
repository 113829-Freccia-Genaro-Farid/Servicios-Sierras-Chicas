import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Persona} from "../../models/persona";
import {MensajeRespuesta} from "../../models/mensaje-respuesta";
import {PersonaDTOPut} from "../../DTOs/persona-dtoput";

@Injectable({
  providedIn: 'root'
})
export class PersonasService {

  constructor(private client:HttpClient) { }

  getDatosPersonaById(idPersona:number): Observable<Persona> {
    return this.client.get<Persona>("http://localhost:8080/api/personas/id/"+ idPersona);
  }

  getDatosPersonaByUser(emailUser:string): Observable<Persona> {
    return this.client.get<Persona>("http://localhost:8080/api/personas/"+ emailUser);
  }
  putPersona(persona:PersonaDTOPut, idPersona:number):Observable<MensajeRespuesta>{
    return this.client.put<MensajeRespuesta>("http://localhost:8080/api/personas/"+idPersona, persona);
  }
}
