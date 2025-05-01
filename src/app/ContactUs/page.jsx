import React from 'react';
import styles from "./contactus.module.css";

const ContactPage = () => {
  return (
    <div className={styles.contactpagecontainer}>
      <section className={styles.contactinfosection}>
        <h2>Contact Information</h2>
        <p>
          If you have any questions or would like to get in touch with us, feel free to contact us using the information below:
        </p>
        <div className={styles.contactdetails}>
          <p>Email: info@jpl.nasa.gov</p>
          <p>Phone: (818) 354-4321</p>
          <p>Address: 4800 Oak Grove Drive, Pasadena, CA 91109</p>
        </div>
      </section>

      <section className={styles.contactformsection}>
        <h2>Contact Form</h2>
        <p>
          Alternatively, you can use the form below to send us a message:
        </p>
        {/* Add your contact form component here */}
        <form>
          {/* Include form fields like name, email, message, etc. */}
          <label htmlFor="name">Name:</label>
          <input type="text" id="name" name="name" />

          <label htmlFor="email">Email:</label>
          <input type="email" id="email" name="email" />

          <label htmlFor="message">Message:</label>
          <textarea id="message" name="message" rows="4" />

          <button type="submit">Send Message</button>
        </form>
      </section>

      {/* Add more sections or components as needed */}
    </div>
    
  );
};

export default ContactPage;
