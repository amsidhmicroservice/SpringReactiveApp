import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { PersonEvent } from "../model/person-event";
import { BehaviorSubject, Observable, Subscriber, Subscription } from "rxjs";
import { map } from "rxjs/operators";
import { Person } from "../model/person";

@Injectable({
  providedIn: 'root'
})
export class PersonServiceService {

  private sseStream!: Subscription;
  private persons!: Person[];

  constructor(private httpClient: HttpClient) {
  }

  getPersonEvent(personEventUrl: string): Observable<PersonEvent> {
    console.log('PersonEventUrl Base Url :' + personEventUrl);
    const httpHeaders = new HttpHeaders()
      .set('content-type', 'text/event-stream')
      .set('Access-Control-Allow-Origin', '*');
    return this.httpClient.get<PersonEventResponseMapping>(personEventUrl, { 'headers': httpHeaders }).pipe(
      map(response => {
        console.log(response._embedded.personEvent);
        return response._embedded.personEvent;
      })
    );
  }

  // Blocking API as it is brings all data in one shot
  getPersons(personUrl: string): Observable<Person[]> {
    console.log('PersonUrl Base Url :' + personUrl);
    return this.httpClient.get<Person[]>(personUrl)
      .pipe(
        map(response => {
          return response;
        })
      );
  }

  //  Non blocking API
  observePersons1(url: string): Observable<any> {
    return new Observable<any>(obs => {
      const es = new EventSource(url);
      
      es.addEventListener('periodic-event', (evt: any) => {
        //console.log(evt.data);
        obs.next(evt.data != null ? JSON.parse(evt.data) : evt.data);
      });


      es.onerror = function (e) {
        if (this.readyState == 0) {
          console.log('The stream has been closed by the server.');
          this.close();
          obs.complete();
        }
      }

      es.onmessage = function (e) {
        console.log("Message Received.Ready State is " + this.readyState);
      }

    });
  }

  //https://thepracticaldeveloper.com/full-reactive-stack-3-angular-rxjs-eventsource/
  // Non Blocking API
  observePersons2(url: string): Observable<any> {
    const eventSource = new EventSource(url);
    return new Observable<any>(observer => {

      eventSource.addEventListener('message', (evt: any) => {
        observer.next(evt.data != null ? JSON.parse(evt.data) : evt.data);
      }); 

      /*  eventSource.onmessage = (event) => {
        //console.log(event.data);
        //let json = JSON.parse(event.data);
        //observer.next(new Quote(json['id'], json['book'], json['content']));
        observer.next(event.data != null ? JSON.parse(event.data) : event.data);
      }; */
 

      eventSource.onerror = function (e) {
        if (this.readyState == 0) {
          console.log('The stream has been closed by the server.');
          this.close();
          observer.complete();
        }
      }

     

    });
  }


}

export interface PersonEventResponseMapping {
  _embedded: {
    personEvent: PersonEvent;
  };
}