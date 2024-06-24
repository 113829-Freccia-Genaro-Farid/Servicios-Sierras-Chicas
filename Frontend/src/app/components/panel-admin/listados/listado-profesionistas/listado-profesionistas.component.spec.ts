import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListadoProfesionistasComponent } from './listado-profesionistas.component';

describe('ListadoProfesionistasComponent', () => {
  let component: ListadoProfesionistasComponent;
  let fixture: ComponentFixture<ListadoProfesionistasComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListadoProfesionistasComponent]
    });
    fixture = TestBed.createComponent(ListadoProfesionistasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
