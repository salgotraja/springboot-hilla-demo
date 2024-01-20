import {
    createBrowserRouter,
    RouteObject
} from "react-router-dom";
import TrainsView from "Frontend/views/TrainsView";
import React from "react";
import MainLayout from "Frontend/views/MainLayout";

/*export const routes: readonly RouteObject[] = [
  { path: "/", element: <TrainsView /> },
];*/

export const routes = [
    {
        element: <MainLayout />,
        handle: { title: 'Hilla CRM' },
        children: [
            { path: '/', element: <TrainsView />, handle: { title: 'Contacts' } },
            /*{ path: '/about', element: <AboutView />, handle: { title: 'About' } },*/
        ],
    },
] as RouteObject[];

export const router = createBrowserRouter([...routes], {basename: new URL(document.baseURI).pathname });
