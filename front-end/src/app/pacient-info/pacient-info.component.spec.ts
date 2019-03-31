import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PacientInfoComponent } from './pacient-info.component';

describe('PacientInfoComponent', () => {
  let component: PacientInfoComponent;
  let fixture: ComponentFixture<PacientInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PacientInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PacientInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
