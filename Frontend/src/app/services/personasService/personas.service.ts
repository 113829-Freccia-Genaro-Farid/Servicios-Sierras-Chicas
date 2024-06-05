import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Persona} from "../../models/persona";

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
}
