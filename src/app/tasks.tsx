import { Card, CardContent, CardHeader } from '@/components/ui/card'
import { Binoculars, Ruler, ZoomIn} from 'lucide-react'
import { ReactNode } from 'react'

export default function Features() {
    return (
        <section className="bg-zinc-50 py-16 md:py-32 dark:bg-transparent">
            <div className="@container mx-auto max-w-5xl px-6">
                <div className="text-center">
                    <h2 className="text-balance text-4xl font-semibold lg:text-5xl">Tasks</h2>
                    <p className="mt-4">We focus on three primary tasks to help explore and study the Moon's surface.</p>
                </div>
                <div className="mx-auto mt-8 grid grid-cols-1 gap-6 text-center md:grid-cols-2 lg:grid-cols-3 md:mt-16 max-w-5xl">
                <Card className="group shadow-zinc-950/5">
                        <CardHeader className="pb-3">
                            <CardDecorator>
                                <Binoculars className="size-6" aria-hidden />
                            </CardDecorator>

                            <h3 className="mt-6 font-medium">Scouting</h3>
                        </CardHeader>

                        <CardContent>
                            <p className="text-sm">Count and mark all visible rocks in each image to support surface mapping efforts.</p>
                        </CardContent>
                    </Card>

                    <Card className="group shadow-zinc-950/5">
                        <CardHeader className="pb-3">
                            <CardDecorator>
                                <Ruler className="size-6" aria-hidden />
                            </CardDecorator>

                            <h3 className="mt-6 font-medium">Sizing</h3>
                        </CardHeader>

                        <CardContent>
                            <p className="mt-3 text-sm">Draw circles around rocks to help estimate their relative sizes for further analysis.</p>
                        </CardContent>
                    </Card>

                    <Card className="group shadow-zinc-950/5">
                        <CardHeader className="pb-3">
                            <CardDecorator>
                                <ZoomIn className="size-6" aria-hidden />
                            </CardDecorator>

                            <h3 className="mt-6 font-medium">Classification</h3>
                        </CardHeader>

                        <CardContent>
                            <p className="mt-3 text-sm">Categorize rocks by shape—such as round, jagged, or irregular—to support scientific research.</p>
                        </CardContent>
                    </Card>
                </div>
            </div>
        </section>
    )
}

const CardDecorator = ({ children }: { children: ReactNode }) => (
    <div className="relative mx-auto size-36 duration-200 [--color-border:color-mix(in_oklab,var(--color-zinc-950)10%,transparent)] group-hover:[--color-border:color-mix(in_oklab,var(--color-zinc-950)20%,transparent)] dark:[--color-border:color-mix(in_oklab,var(--color-white)15%,transparent)] dark:group-hover:bg-white/5 dark:group-hover:[--color-border:color-mix(in_oklab,var(--color-white)20%,transparent)]">
        <div aria-hidden className="absolute inset-0 bg-[linear-gradient(to_right,var(--color-border)_1px,transparent_1px),linear-gradient(to_bottom,var(--color-border)_1px,transparent_1px)] bg-[size:24px_24px]" />
        <div aria-hidden className="bg-radial to-background absolute inset-0 from-transparent to-75%" />
        <div className="bg-background absolute inset-0 m-auto flex size-12 items-center justify-center border-l border-t">{children}</div>
    </div>
)