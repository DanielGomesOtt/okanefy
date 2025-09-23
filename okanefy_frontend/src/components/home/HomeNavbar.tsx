import {
  Navbar,
  NavbarBrand,
} from "@heroui/react";

export const OkanefyLogo = () => {
  return (
    <div className="mt-5">
        <img src="src\assets\okanefy_logo.png" className="w-2/6"></img>
    </div>
  );
};

export default function HomeNavbar() {

  return (
      <Navbar className="justify-between xl:pl-56">
        <NavbarBrand>
          <OkanefyLogo/>
        </NavbarBrand>
      </Navbar>
  );
}

