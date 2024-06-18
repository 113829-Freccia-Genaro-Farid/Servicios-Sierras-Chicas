import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListadoAnunciosComponent } from './listado-anuncios.component';

describe('ListadoAnunciosComponent', () => {
  let component: ListadoAnunciosComponent;
  let fixture: ComponentFixture<ListadoAnunciosComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListadoAnunciosComponent]
    });
    fixture = TestBed.createComponent(ListadoAnunciosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
