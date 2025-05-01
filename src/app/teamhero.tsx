import { Button } from '@/components/ui/button'
import { ChevronRight } from 'lucide-react'
import Link from 'next/link'

export default function ContentSection() {
    return (
        <section className="py-16 md:py-32">
            <div className="mx-auto max-w-5xl space-y-8 px-6 md:space-y-12">
                <img className="rounded-(--radius) grayscale" src="/OurTeam/group-pic.jpg" alt="team image" height="" width="" loading="lazy" />

                <div className="grid gap-6 md:grid-cols-2 md:gap-12">
                    <h2 className="text-4xl font-medium">Building the Future of Lunar Science, Together.</h2>
                    <div className="space-y-6">
                        <p>Our project brings together computer science, engineering, and passion. As part of the Lunar Rocks initiative, we’re developing real solutions for real missions—supporting NASA’s Viper rover with precision tools for scientific discovery.</p>

                        <Button asChild variant="secondary" size="sm" className="gap-1 pr-1.5">
                            <Link href="/AboutUs">
                                <span>About Us</span>
                                <ChevronRight className="size-2" />
                            </Link>
                        </Button>
                    </div>
                </div>
            </div>
        </section>
    )
}
