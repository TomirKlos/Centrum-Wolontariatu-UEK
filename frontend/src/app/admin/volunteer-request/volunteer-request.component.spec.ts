import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VolunteerRequestComponent } from './volunteer-request.component';

describe('VolunteerRequestComponent', () => {
  let component: VolunteerRequestComponent;
  let fixture: ComponentFixture<VolunteerRequestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VolunteerRequestComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VolunteerRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
