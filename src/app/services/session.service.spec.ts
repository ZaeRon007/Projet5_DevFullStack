import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let sessionService: SessionService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionService]
    });

    sessionService = TestBed.inject(SessionService);
    httpMock = TestBed.inject(HttpTestingController);

  });

  afterEach(() => {
    httpMock.verify();
  })

  it('should be created', () => {
    expect(sessionService).toBeTruthy();
  });

  it('should log in user', () => {
    let expectedSession: SessionInformation = {
      token: "ad52gf456j6k98h2g1de489y32csw45xw5c6nh9ik9i",
      type: "type",
      id: 1,
      username: "pedro@gmail.com",
      firstName: "john",
      lastName: "doe",
      admin: false}

    sessionService.$isLogged().subscribe((response) => {
      expect(response).toBe(true);
    })  

    sessionService.logIn(expectedSession);

    expect(sessionService.sessionInformation).toEqual(expectedSession);
    expect(sessionService.isLogged).toBe(true);

  })

  it('should log out user', () => {
    sessionService.$isLogged().subscribe((response) => {
      expect(response).toBe(false);
    })  

    sessionService.logOut();

    expect(sessionService.sessionInformation).toBe(undefined);
    expect(sessionService.isLogged).toBe(false);    
  })

});
