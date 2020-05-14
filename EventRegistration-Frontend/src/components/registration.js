import _ from 'lodash';
import axios from 'axios';
let config = require('../../config');

let backendConfigurer = function () {
  switch (process.env.NODE_ENV) {
    case 'testing':
    case 'development':
      return 'http://' + config.dev.backendHost + ':' + config.dev.backendPort;
    case 'production':
      return 'https://' + config.build.backendHost + ':' + config.build.backendPort;
  }
}

let backendUrl = backendConfigurer();

let AXIOS = axios.create({
  baseURL: backendUrl
  // headers: {'Access-Control-Allow-Origin': frontendUrl}
});

export default {
  name: 'eventregistration',
  data () {
    return {
      persons: [],
      promoters: [],
      events: [],
      theatres: [],
      newPerson: '',
      personType: 'Person',
      newEvent: {
        name: '',
        date: '2017-12-08',
        startTime: '09:00',
        endTime: '11:00'
      },
      selectedPerson: '',
      selectedPromoter: '',
      selectedEvent: '',
      selectedPersonPay: '',
      selectedEventPay: '',
      userID: '',
      amount: '',
      errorPerson: '',
      errorEvent: '',
      errorRegistration: '',
      errorPaypal: '',
      response: []
    }
  },
  created: function () {
    // Initializing persons
    AXIOS.get('/persons')
    .then(response => {
      this.persons = response.data;
      this.persons.forEach(person => this.getRegistrations(person.name))
    })
    .catch(e => { this.errorPerson = e });

    AXIOS.get('/events').then(response => { this.events = response.data }).catch(e => { this.errorEvent = e });
  },

  methods: {

    createPerson: function (personType, personName) {
      if (personType === 'Person') {
        AXIOS.post('/persons/'.concat(personName), {}, {})
          .then(response => {
            this.persons.push(response.data);
            this.errorPerson = '';
            this.newPerson = '';
          })
          .catch(e => {
            e = e.response.data.message ? e.response.data.message : e;
            this.errorPerson = e;
            console.log(e);
          });
      } else if (personType === 'Promoter') {
        AXIOS.post('/promoters/'.concat(personName), {}, {})
          .then(response => {
            this.promoters.push(response.data);
            this.persons.push(response.data);
            this.errorPerson = '';
            this.newPerson = '';
          })
          .catch(e => {
            e = e.response.data.message ? e.response.data.message : e;
            this.errorPerson = e;
            console.log(e);
          });
      }
    },

    createEvent: function (newEvent) {
      if (!newEvent.title) {
        let event = {
          'name': newEvent.name,
          'date': newEvent.date,
          'startTime': newEvent.startTime,
          'endTime': newEvent.endTime
        }
        if (!this.events.find(x => x.name === newEvent.name)) { this.events.push(event); }
        let params = {
          'date': newEvent.date,
          'startTime': newEvent.startTime,
          'endTime': newEvent.endTime
        }
        AXIOS.post('/events/'.concat(newEvent.name), {}, {params: params})
          .then(response => {
            this.errorEvent = '';
            this.newEvent.name = this.newEvent.date = this.newEvent.startTime = this.newEvent.endTime = '';
          })
          .catch(e => {
            e = e.response.data.message ? e.response.data.message : e;
            this.errorEvent = e;
            console.log(e);
          });
      } else {
        let event = {
          'name': newEvent.name,
          'date': newEvent.date,
          'startTime': newEvent.startTime,
          'endTime': newEvent.endTime,
          'title': newEvent.title
        }
        if (!this.events.find(x => x.name === newEvent.name)) {
          this.theatres.push(event);
          this.events.push(event);
        }
        let params = {
          'date': newEvent.date,
          'startTime': newEvent.startTime,
          'endTime': newEvent.endTime,
          'title': newEvent.title
        }
        AXIOS.post('/theatres/'.concat(newEvent.name), {}, {params: params})
          .then(response => {
            this.errorEvent = '';
            this.newEvent.name = this.newEvent.date = this.newEvent.startTime = this.newEvent.endTime = this.newEvent.title = '';
          })
          .catch(e => {
            e = e.response.data.message ? e.response.data.message : e;
            this.errorEvent = e;
            console.log(e);
          })
      }
    },

    registerEvent: function (personName, eventName) {
      let event = this.events.find(x => x.name === eventName);
      let person = this.persons.find(x => x.name === personName);
      let params = {
        person: person.name,
        event: event.name
      };

      AXIOS.post('/register', {}, {params: params})
      .then(response => {
        person.eventsAttended.push(event)
        this.selectedPerson = '';
        this.selectedEvent = '';
        this.errorRegistration = '';
      })
      .catch(e => {
        e = e.response.data.message ? e.response.data.message : e;
        this.errorRegistration = e;
        console.log(e);
      });
    },

    makePayment: function (personName, eventName, userID, amount) {
      let event = this.events.find(x => x.name === eventName);
      let person = this.persons.find(x => x.name === personName);
      let params = {
        person: person.name,
        event: event.name,
        userID,
        amount
      };

      AXIOS.post('/pay', {}, {params: params})
        .then(response => {
          this.persons.forEach(person => this.getRegistrations(person.name));
          this.selectedPerson = '';
          this.selectedEvent = '';
          this.userID = '';
          this.amount = '';
          this.errorPaypal = '';
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          this.userID = '';
          this.amount = '';
          this.errorPaypal = e;
          console.log(e);
        });
    },

    assignEvent: function (promoterName, eventName) {
      let event = this.events.find(x => x.name === eventName);
      let promoter = this.promoters.find(x => x.name === promoterName);
      let params = {
        promoter: promoter.name,
        event: event.name
      };

      AXIOS.post('/assign', {}, {params: params})
        .then(response => {
          this.selectedPromoter = '';
          this.selectedEvent = '';
          this.errorEvent = '';
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          this.errorEvent = e;
          console.log(e);
        });
    },

    getRegistrations: function (personName) {
      AXIOS.get('/events/person/'.concat(personName))
        .then(response => {
          if (!response.data || response.data.length <= 0) return;

          let i = this.persons.map(x => x.name).indexOf(personName);
          this.persons[i].eventsAttended = [];
          response.data.forEach(event => {
            let promise = this.getPayments(this.persons[i].name, event.name);
            promise.then(response => {
              event.userID = response.email,
              event.amount = response.amount,
              this.persons[i].eventsAttended.push(event);
            })
          });
        })
        .catch(e => {
          e = e.response.data.message ? e.response.data.message : e;
          console.log(e);
        });
    },

    getPayments: function (personName, eventName) {
      return new Promise(function (resolve, reject) {
        AXIOS.get('/registrations/' + personName + '/' + eventName)
          .then(response => {
            if (!response.data || response.data.length <= 0) reject();
            resolve(response.data)
          })
          .catch(e => {
            e = e.response.data.message ? e.response.data.message : e;
            console.log(e);
          });
      })
    }
  }
}
