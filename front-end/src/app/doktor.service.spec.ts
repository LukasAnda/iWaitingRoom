import { TestBed } from '@angular/core/testing';

import { DoktorService } from './doktor.service';

describe('DoktorService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DoktorService = TestBed.get(DoktorService);
    expect(service).toBeTruthy();
  });
});
