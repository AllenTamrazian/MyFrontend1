import Link from 'next/link'
import Image from 'next/image';

const members = [
    {
        name: 'Hovhannes Babayan',
        role: 'Architecture/design lead',
        avatar: "/OurTeam/hovhannes-babyan.jpg",
        link: '#',
    },
    {
        name: 'Steven Chen',
        role: 'Project Lead',
        avatar: '/OurTeam/steven-chen.jpg',
        link: 'https://github.com/kylochen53',
    },
    {
        name: 'George Ewest',
        role: 'Database Schema lead',
        avatar: '/OurTeam/george-ewest.jpg',
        link: '#',
    },
    {
        name: 'Hober Granados',
        role: 'Customer liaison/requirements lead',
        avatar: '/OurTeam/hober-granados.jpg',
        link: '#',
    },
    {
        name: 'Jaden Lazo',
        role: 'Documentation Lead',
        avatar: '/OurTeam/jaden-lazo.jpg',
        link: '#',
    },
    {
        name: 'Jacky Man',
        role: 'Project Consultant',
        avatar: '/OurTeam/jacky-man.jpg',
        link: '#',
    },
    {
        name: 'Barnabas Novak',
        role: 'Database Schema lead',
        avatar: '/OurTeam/barnabas-novak.jpg',
        link: '#',
    },
    {
        name: 'Nathan Rodriguez-Lyn',
        role: 'Backend and component lead',
        avatar: '/OurTeam/nathan-rodriguez-lyn.jpg',
        link: '#',
    },
    {
        name: 'Allen Tamrazian',
        role: 'Presentation Lead',
        avatar: '/OurTeam/allen-tamrazian.jpg',
        link: '#',
    },
    {
        name: 'Jordy Ye Cao',
        role: 'UI Lead',
        avatar: '/OurTeam/jordy-ye-cao.jpg',
        link: '#',
    },
]

export default function TeamSection() {
    return (
        <section className="bg-gray-50 py-16 md:py-32 dark:bg-transparent">
            <div className="mx-auto max-w-5xl border-t px-6">

                <span className="text-caption -ml-6 -mt-3.5 block w-max bg-gray-50 px-6 dark:bg-gray-950">
                      <Image
                          src="https://upload.wikimedia.org/wikipedia/commons/4/42/CalStateLA_Logo.svg"
                          alt="CSULA Logo"
                          width={40}
                          height={40}
                          className="inline-block"
                      />
                     Team</span>
                <div className="mt-12 gap-2 sm:grid sm:grid-cols-2 md:mt-24">
                    <div className="sm:w-2/5">
                        <h2 className="text-3xl font-bold sm:text-4xl">Built by Students, Powered by Purpose</h2>
                    </div>
                    <div className="mt-6 sm:mt-0">
                        <p>Rooted at CSULA, our team is driven by a passion for innovation, a commitment to learning, and a belief that technology can empower communities and redefine what’s possible.</p>
                    </div>
                </div>
                <div className="mt-12 md:mt-24">
                    <div className="grid gap-x-6 gap-y-12 sm:grid-cols-2 lg:grid-cols-3">
                        {members.map((member, index) => (
                            <div key={index} className="group overflow-hidden">
                                <img className="h-96 w-full rounded-md object-cover object-top grayscale transition-all duration-500 hover:grayscale-0 group-hover:h-[22.5rem] group-hover:rounded-xl" src={member.avatar} alt="team member" width="826" height="1239" />
                                <div className="px-2 pt-2 sm:pb-0 sm:pt-4">
                                    <div className="flex justify-between">
                                        <h3 className="text-title text-base font-medium transition-all duration-500 group-hover:tracking-wider">{member.name}</h3>
                                        <span className="text-xs">_0{index + 1}</span>
                                    </div>
                                    <div className="mt-1 flex items-center justify-between">
                                        <span className="text-muted-foreground inline-block translate-y-6 text-sm opacity-0 transition duration-300 group-hover:translate-y-0 group-hover:opacity-100">{member.role}</span>
                                        <Link href={member.link} className="group-hover:text-primary-600 dark:group-hover:text-primary-400 inline-block translate-y-8 text-sm tracking-wide opacity-0 transition-all duration-500 hover:underline group-hover:translate-y-0 group-hover:opacity-100">
                                            {' '}
                                            GitHub
                                        </Link>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </section>
    )
}