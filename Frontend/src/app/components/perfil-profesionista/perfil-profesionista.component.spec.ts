import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerfilProfesionistaComponent } from './perfil-profesionista.component';

describe('PerfilProfesionistaComponent', () => {
  let component: PerfilProfesionistaComponent;
  let fixture: ComponentFixture<PerfilProfesionistaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PerfilProfesionistaComponent]
    });
    fixture = TestBed.createComponent(PerfilProfesionistaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
