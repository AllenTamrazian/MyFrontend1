import React from 'react';
import Link from "next/link";
import { getServerSession } from 'next-auth';
import { redirect } from 'next/navigation';
import { options } from '../api/auth/[...nextauth]/options';
import Image from "next/image";

import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"

import { Button } from "@/components/ui/button"


const ExplorePage = async () => {

const session = await getServerSession(options);

if (!session) {
  redirect('/api/auth/signin?callbackUrl=/Explore');
}

  return (
      <div className="w-full min-h-screen flex flex-col items-center justify-center bg-white px-4 py-12">
        <h1 className="text-4xl font-bold text-gray-800 mb-10 text-center">
          Explore Tasks
        </h1>
        <div className="flex flex-wrap justify-center gap-8 max-w-7xl">
          {/* SCOUT CARD */}
          <Card className="w-[300px]">
            <CardHeader>
              <CardTitle>Scout</CardTitle>
              <CardDescription>Identify and mark rocks on the surface</CardDescription>
            </CardHeader>
            <CardContent>
              <Image
                  src="/assets/task_icons/scouting.PNG"      // your image path
                  alt="Moon Rock"
                  width={300}
                  height={200}
                  className="rounded-md object-cover"
              />
            </CardContent>
            <CardFooter className="flex flex-col items-start space-y-2">
              <Button asChild  className="bg-gray-500 hover:bg-red-600 text-white font-bold py-2 px-4">
              <Link href="./Tasks/Scouting">
                  Scout
              </Link>
              </Button>
              <p>Click to begin the scouting task.</p>
            </CardFooter>
          </Card>

          {/* SIZE CARD */}
          <Card className="w-[300px]">
            <CardHeader>
              <CardTitle>Size</CardTitle>
              <CardDescription>Estimate rock sizes for analysis</CardDescription>
            </CardHeader>
            <CardContent>
              <Image
                  src="/assets/task_icons/sizing.PNG"      // your image path
                  alt="Moon Rock"
                  width={300}
                  height={200}
                  className="rounded-md object-cover"
              />
            </CardContent>
            <CardFooter className="flex flex-col items-start space-y-2">
              <Button asChild  className="bg-gray-500 hover:bg-red-600 text-white font-bold py-2 px-4">
              <Link href="./Tasks/Sizing">
                  Size
              </Link>
              </Button>
              <p>Click to begin the sizing task.</p>
            </CardFooter>
          </Card>

          {/* CLASSIFY CARD */}
          <Card className="w-[300px]">
            <CardHeader>
              <CardTitle>Classify</CardTitle>
              <CardDescription>Categorize rocks based on type</CardDescription>
            </CardHeader>
            <CardContent>
              <Image
                  src="/assets/task_icons/classification.PNG"      // your image path
                  alt="Moon Rock"
                  width={300}
                  height={200}
                  className="rounded-md object-cover"
              />
            </CardContent>
            <CardFooter className="flex flex-col items-start space-y-2">
              <Button asChild  className="bg-gray-500 hover:bg-red-600 text-white font-bold py-2 px-4">
              <Link href="./Tasks/Classifying">
                  Classify
              </Link>
              </Button>
              <p>Click to begin the classification task.</p>
            </CardFooter>
          </Card>
        </div>
      </div>
  );
};

export default ExplorePage;