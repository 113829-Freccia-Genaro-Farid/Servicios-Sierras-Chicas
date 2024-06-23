import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PanelProfesionistaComponent } from './panel-profesionista.component';

describe('PanelProfesionistaComponent', () => {
  let component: PanelProfesionistaComponent;
  let fixture: ComponentFixture<PanelProfesionistaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PanelProfesionistaComponent]
    });
    fixture = TestBed.createComponent(PanelProfesionistaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
