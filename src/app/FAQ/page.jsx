import React from 'react';
import styles from "./faq.module.css";
import FAQsTwo from "./faq.tsx";
// import {
//   Accordion,
//   AccordionContent,
//   AccordionItem,
//   AccordionTrigger,
// } from "@/components/ui/accordion"

const QuestionsPage = () => {
  const faqs = [
    {
      question: 'What is Viper Rocks?',
      answer: 'Viper Rocks is a space exploration organization dedicated to...',
    },
    {
      question: 'How can I join Viper Rocks?',
      answer: 'To join Viper Rocks, you can visit our "Join Us" page and follow the...',
    },
    {
      question: 'Do you offer educational programs?',
      answer: 'Yes, we have various educational programs aimed at inspiring the next...',
    },
    // Add more FAQs as needed
  ];

  return (
      // <div className="min-h-screen flex flex-col">
      //   <main className="flex-grow p-8">
      // <div className={styles.questionspagecontainer}>
      //   <section className={styles.faqssection}>
      //     <h2 className="mb-6">Frequently Asked Questions</h2>
      //     <div className={styles.faqlist}>
      //       {faqs.map((faq, index) => (
      //           <div key={index} className={styles.faqitem}>
      //             <Accordion type="single" collapsible>
      //               <AccordionItem value="item-1">
      //                 <AccordionTrigger> <h3>{faq.question}</h3></AccordionTrigger>
      //                 <AccordionContent>
      //                   <p>{faq.answer}</p>
      //                 </AccordionContent>
      //               </AccordionItem>
      //             </Accordion>
      //           </div>
      //       ))}
      //     </div>
      //   </section>
      //   {/* Add more sections or components as needed */}
      // </div>
      //   </main>
      // </div>
      <FAQsTwo />
  );
};

export default QuestionsPage;
