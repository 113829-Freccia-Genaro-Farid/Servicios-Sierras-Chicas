import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraficoUsuariosNuevosComponent } from './grafico-usuarios-nuevos.component';

describe('GraficoUsuariosNuevosComponent', () => {
  let component: GraficoUsuariosNuevosComponent;
  let fixture: ComponentFixture<GraficoUsuariosNuevosComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GraficoUsuariosNuevosComponent]
    });
    fixture = TestBed.createComponent(GraficoUsuariosNuevosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
