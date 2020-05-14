<template>
  <div id="eventregistration">

    <h2>Persons</h2>
    <table id="persons-table">
      <tr>
        <th>Name</th>
        <th>Events</th>
        <th>Payment ID</th>
        <th>Amount ($)</th>
      </tr>
      <tr v-for="(person, i) in persons" v-bind:key="`person-${i}`">
        <td>{{person.name}}</td>
        <td>
          <ul>
            <li v-for="(event, i) in person.eventsAttended" v-bind:key="`event-${i}`" style="list-style-type: disc;">
              <span class='registration-event-name'>{{event.name}}</span>
            </li>
          </ul>
        </td>
		    <td>
          <ul>
            <li v-for="(event, i) in person.eventsAttended" v-bind:key="`event-${i}`" style="list-style-type: disc;">
              <span class='registration-event-name'>{{event.userID}}</span>
            </li>
          </ul>
        </td>
		    <td>
          <ul>
            <li v-for="(event, i) in person.eventsAttended" v-bind:key="`event-${i}`" style="list-style-type: disc;">
              <span class='registration-event-name'>{{event.amount}}</span>
            </li>
          </ul>
        </td>
      </tr>
      <tr>
        <td>
          <input id="create-person-person-name" type="text" v-model="newPerson" placeholder="Person Name">
        </td>
		    <td>
          <select id='create-person-person-type' v-model="personType">
            <option value="Person">Person</option>
            <option value="Promoter">Promoter</option>
          </select>
        </td>
        <td>
          <button id="create-person-button" v-bind:disabled="!newPerson" @click="createPerson(personType, newPerson)">Create Person</button>
        </td>
        <td></td>
        <td></td>
      </tr>
    </table>
    <span v-if="errorPerson" style="color:red">Error: {{errorPerson}}</span>
    <hr>

    <h2>Events</h2>
    <table id='events-table'>
      <tr>
        <th>Name</th>
        <th>Date</th>
        <th>Start</th>
        <th>End</th>
		    <th>Title</th>
      </tr>
      <tr v-for="(event, i) in events" v-bind:id="event.name" v-bind:key="`event-${i}`">
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-name`">{{event.name}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-date`">{{event.date}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-starttime`">{{event.startTime}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-endtime`">{{event.endTime}}</td>
        <td v-bind:id="`${event.name.replace(/\s/g, '-')}-title`">{{typeof event.title === "undefined" ? "--" : event.title}}</td>
      </tr>
      <tr>
        <td>
          <input id="event-name-input" type="text" v-model="newEvent.name" placeholder="Event Name">
        </td>
        <td>
          <input id="event-date-input" type="date" v-model="newEvent.date" placeholder="YYYY-MM-DD">
        </td>
        <td>
          <input id="event-starttime-input" type="time" v-model="newEvent.startTime" placeholder="HH:mm">
        </td>
        <td>
          <input id="event-endtime-input" type="time" v-model="newEvent.endTime" placeholder="HH:mm">
        </td>
		     <td>
          <input id="event-title-input" type="text" v-model="newEvent.title" placeholder="Title hint">
        </td>
        <td>
          <button id="event-create-button" v-bind:disabled="!newEvent.name" v-on:click="createEvent(newEvent)">Create</button>
        </td>
      </tr>
    </table>
    <span id="event-error" v-if="errorEvent" style="color:red">Error: {{errorEvent}}</span>
    <hr>

    <h2>Registrations</h2>
    <label>Person:
      <select id='registration-person-select' v-model="selectedPerson">
        <option disabled value="">Please select one</option>
        <option v-for="(person, i) in persons" v-bind:key="`person-${i}`">{{person.name}}</option>
      </select>
    </label>
    <label>Event:
      <select id='registration-event-select' v-model="selectedEvent">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">{{event.name}}</option>
      </select>
    </label>
    <button id='registration-button' v-bind:disabled="!selectedPerson || !selectedEvent" @click="registerEvent(selectedPerson, selectedEvent)">Register</button>
    <br/>
    <span v-if="errorRegistration" style="color:red">Error: {{errorRegistration}}</span>
    <hr>

	<h2>Assign Promoters</h2>
    <label>Promoter:
      <select id='assign-selected-promoter' v-model="selectedPromoter">
        <option disabled value="">Please select one</option>
        <option v-for="(promoter, i) in promoters" v-bind:key="`promoter-${i}`">{{promoter.name}}</option>
      </select>
    </label>
    <label>Event:
      <select id='assign-selected-event-promoter' v-model="selectedEvent">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">{{event.name}}</option>
      </select>
    </label>
    <button id='assign-button-promoter' v-bind:disabled="!selectedEvent || !selectedEvent" @click="assignEvent(selectedPromoter, selectedEvent)">Assign</button>
    <br/>
    <span v-if="errorRegistration" style="color:red">Error: {{errorRegistration}}</span>
	 <hr>

    <h2>Pay for Registration with Paypal</h2>
    <label>Person:
      <select id='paypal-person-select' v-model="selectedPersonPay">
        <option disabled value="">Please select one</option>
        <option v-for="(person, i) in persons" v-bind:key="`person-${i}`">{{person.name}}</option>
      </select>
    </label>
    <label>Event:
      <select id='paypal-event-select' v-model="selectedEventPay">
        <option disabled value="">Please select one</option>
        <option v-for="(event, i) in events" v-bind:key="`event-${i}`">{{event.name}}</option>
      </select>
    </label>
    <br>
      <label>Device Id:
        <input id="paypal-id-input" type="text" v-model="userID" placeholder="Email">
      </label>
      <label>Amount:
        <input id="paypal-amount-input" type="number" v-model="amount" step="$">
      </label>
    <br>
    <button id='paypal-button' v-bind:disabled="!selectedPersonPay || !selectedEventPay" @click="makePayment(selectedPersonPay, selectedEventPay, userID, amount)">Make payment</button>
    <br/>
    <span id="paypal-error" v-if="errorPaypal" style="color:red">Error: {{errorPaypal}}</span>
    <hr>

  </div>
</template>

<script src="./registration.js"></script>

<style>
#eventregistration {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background: #f2ece8;
  margin-top: 60px;
}
.registration-event-name {
  display: inline-block;
  width: 25%;
}
.registration-event-name {
  display: inline-block;
}
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  text-align: left;
}
a {
  color: #42b983;
}
</style>
