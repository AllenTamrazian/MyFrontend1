import styles from "./aboutus.module.css";
import TeamSection from './team';

const AboutUs = () => {
  return (
    // <div className={styles.aboutuscontainer}>
    //   <section className={styles.aboutuscontent}>
    //     <h2>Welcome to Our Story</h2>
    //     <p>
    //       At Viper Rocks, we are passionate about exploring the wonders of the universe.
    //       Our mission is to bring the excitement of space exploration to everyone.
    //     </p>
    //
    //   </section>
    //
    //   <section className={styles.teamsection}>
    //     <h2>Meet Our Team</h2>
    //     <div className={styles.teammember}>
    //       <div className={styles.frontend}>
    //         <h4>Frontend Team</h4>
    //             <ul>
    //                 <li>Jordy Ye Cao</li>
    //                 <li>Hober Granados</li>
    //                 <li>Nathan Rodriguez-Lynn</li>
    //                 <li>Allen Tamrazian</li>
    //                 <li>George Ewest</li>
    //             </ul>
    //
    //       <div className={styles.backend}>
    //         <h4>Backend Team</h4>
    //             <ul>
    //                 <li>Steven Chen</li>
    //                 <li>Hovhannes Babayan</li>
    //                 <li>Jacky Man</li>
    //                 <li>Barnabas Novak</li>
    //                 <li>Jaden Lazo</li>
    //             </ul>
    //         </div>
    //     </div>
    //
    //     </div>
    //     {/* Add more team members as needed */}
    //   </section>
    //
    //   <section className={styles.valuessection}>
    //     <h2>Our Values</h2>
    //     <ul>
    //       <li>Curiosity</li>
    //       <li>Innovation</li>
    //       <li>Collaboration</li>
    //       <li>Education</li>
    //     </ul>
    //   </section>
    // </div>
      <section className={styles.teamsection}>
          <TeamSection />
      </section>
  );
};

export default AboutUs;
