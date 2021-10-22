import { ChangeDetectorRef, Component } from '@angular/core';
import { Person } from "./model/person";
import { PersonServiceService } from "./services/person-service.service";
import { PersonEvent } from "./model/person-event";
import { map, take } from "rxjs/operators";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'SpringReactiveAngular';

  persons: Person[] = []
  personEvent!: PersonEvent;
  readonly personEventUrl: string = "http://localhost:8282/persons/1/events";
  readonly personUrl: string = "http://localhost:8282/persons";
  readonly personStreamUrl1: string = "http://localhost:8282/persons/stream1";
  readonly personStreamUrl2: string = "http://localhost:8282/persons/stream2";

  constructor(private personServiceService: PersonServiceService, private cdr: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    //this.getPersonList();
      this.observePersons1();
    //this.observePersons2();

    //this.getPersonEvent();
    
  }

  getPersonList() {
    this.personServiceService.getPersons(this.personUrl).subscribe(responseData => {
      console.log(responseData);
      this.persons = responseData;
    });
  }

  getPersonEvent() {
    this.personServiceService.getPersonEvent(this.personEventUrl).subscribe(responseData => {
      this.personEvent = responseData;
    });
  }

  observePersons1() {
    this.personServiceService.observePersons1(this.personStreamUrl1)
      .subscribe((person: Person) => {
        console.log(person);
        this.persons.push(person);
      });
  }

  observePersons2() {
    this.personServiceService.observePersons2(this.personStreamUrl2)
      .subscribe((person: Person) => {
        console.log(person);
        this.persons.push(person);
        this.cdr.detectChanges();
      });
  }

}


