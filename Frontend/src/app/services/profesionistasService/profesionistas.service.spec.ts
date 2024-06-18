import { TestBed } from '@angular/core/testing';

import { ProfesionistasService } from './profesionistas.service';

describe('ProfesionistasService', () => {
  let service: ProfesionistasService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProfesionistasService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
