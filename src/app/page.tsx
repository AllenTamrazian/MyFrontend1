import Image from "next/image";
import { DarkButton } from "./(components)/Button";
import { BlockCardGroup } from "./(components)/Blocks";
import OurTeamData from "@/data/ourTeam.json";
import Features from "./tasks";
import ContentSection from "@/app/teamhero";
import {Hero1} from "@/app/hero";
export default function Home() {
  return (
    <main className="flex min-h-screen flex-col">
      {/*our old content*/}
      {/*<div*/}
      {/*  id="#"*/}
      {/*  className="min-h-screen flex justify-center items-center text-center"*/}
      {/*>*/}
      {/*  <div*/}
      {/*    className="absolute min-h-screen min-w-full bg-cover bg-center text-center"*/}
      {/*    style={{ backgroundImage: "url(/moon101-the-lunar-surface.jpg)" }}*/}
      {/*  ></div>*/}
      {/*  <div className="relative text-center flex flex-col p-24">*/}
      {/*    <span className="text-h1-alt text-white">LUNAR ROCKS</span>*/}
      {/*    <span className="text-h3 text-white">*/}
      {/*      Citizen Scientists Needed. Sign up to identify moon rocks for Viper*/}
      {/*      rover expeditions.*/}
      {/*    </span>*/}
      {/*    <DarkButton href="/Explore"> Explore </DarkButton>*/}
      {/*  </div>*/}
      {/*</div>*/}
        <section className="w-full flex justify-center items-center min-h-[80vh] bg-gray">
          {/*Main Hero */}
      <Hero1 heading={"Lunar Rocks"} description={"Become a space explorer from your laptop.\n" +
          "Help scientists identify and classify moon rocks to guide the Viper rover."} image={{ src: "/hero.jpg", alt: "Lunar surface background" }}/>
        </section>
      <div id="#about-us">
        <div className="bg-black" style={{ backgroundImage: "url(/space.webp)" }}>
          <div id="our-story" className="p-24 flex">
            <span className="text-h2 text-white mr-10">OUR STORY</span>
            <span className="text-subtitle flex justify-center items-center text-white">
              At Lunar Rocks, we are passionate about exploring the wonders of
              the universe. Our mission is to bring the excitement of space
              exploration to everyone. Our Values: Curiosity Innovation
              Collaboration Education
            </span>
          </div>
        </div>
        {/*Hero for team (Group photo) */}
        <ContentSection />
        <div
          className="relative flex flex-col p-24 bg-fixed bg-center bg-no-repeat bg-cover"
          style={{ backgroundImage: "url(/space.webp)" }}
        >
          {/*<span className="text-h1 mb-10 text-center text-white">*/}
          {/*  MEET THE TEAM*/}
          {/*</span>*/}
          {/*<div>*/}
          {/*  <BlockCardGroup cards={OurTeamData} />*/}
          {/*</div>*/}
          {/*Features which are the tasks cards on the page*/}
          <Features />
        </div>
      </div>
    </main>
  );
}
