import {
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  Button,
} from "@heroui/react";

export const OkanefyLogo = () => {
  return (
    <div>
        <img src="src\assets\okanefy_logo.png" className="hidden md:w-3/6 md:block"></img>
        <img src="src\assets\logo_reduzido_okanefy.png" className="w-3/6 md:hidden"></img>
    </div>
  );
};

export default function HomeNavbar() {

  return (
      <Navbar>
        <NavbarContent>
            <NavbarBrand>
              <OkanefyLogo />
            </NavbarBrand>
        </NavbarContent>


        <NavbarContent justify="end">
            <NavbarItem>
                <Button className="bg-lime-700 text-white" href="#" variant="flat">
                    Sign In
                </Button>
            </NavbarItem>
            <NavbarItem>
                <Button className="bg-lime-700 text-white" href="#" variant="flat">
                    Sign Up
                </Button>
            </NavbarItem>
        </NavbarContent>
      </Navbar>
  );
}

