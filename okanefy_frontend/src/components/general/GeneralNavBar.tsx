import React from "react";
import {
  Button,
  Link,
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  NavbarMenu,
  NavbarMenuItem,
  NavbarMenuToggle,
} from "@heroui/react";
import { LogoutIcon } from "../icons/LogoutIcon";
import { AccountIcon } from "../icons/AccountIcon";

const OkanefyLogo = () => {
  return (
    <div>
      <img src="src/assets/okanefy_logo.png" className="w-3/6" alt="Okanefy Logo" />
    </div>
  );
};

export default function GeneralNavBar() {
  const [isMenuOpen, setIsMenuOpen] = React.useState(false);

  const menuItems = [
    { label: "Categorias", href: "#" },
    { label: "Formas de pagamentos", href: "#" },
    { label: "Relatórios", href: "#" },
    { label: "Conta", href: "/profile", icon: <AccountIcon /> },
    { label: "Logout", href: "#", icon: <LogoutIcon />, danger: true },
  ];

  function logout() {
    localStorage.removeItem("id");
    localStorage.removeItem("name");
    localStorage.removeItem("email");
    localStorage.removeItem("token");
    window.location.reload();
  }

  return (
    <Navbar
      position="static"
      className="shadow-xl"
      isMenuOpen={isMenuOpen}
      onMenuOpenChange={setIsMenuOpen}
    >
      
      <NavbarContent justify="start">
        <NavbarBrand>
          <OkanefyLogo />
        </NavbarBrand>
      </NavbarContent>

     
      <NavbarContent className="hidden sm:flex gap-4" justify="center">
        <NavbarItem>
          <Link color="foreground" href="#">
            Categorias
          </Link>
        </NavbarItem>
        <NavbarItem isActive>
          <Link aria-current="page" href="#">
            Formas de pagamentos
          </Link>
        </NavbarItem>
        <NavbarItem>
          <Link color="foreground" href="#">
            Relatórios
          </Link>
        </NavbarItem>
      </NavbarContent>

      
      <NavbarContent justify="end" className="sm:hidden">
        <NavbarMenuToggle
          aria-label={isMenuOpen ? "Fechar menu" : "Abrir menu"}
          className="cursor-pointer"
        />
      </NavbarContent>
      <NavbarContent justify="end" className="hidden sm:flex">
        <NavbarItem>
          <Button
            color="primary"
            startContent={<AccountIcon />}
            as={Link}
            href="/profile"
          >
            Conta
          </Button>
        </NavbarItem>
        <NavbarItem>
          <Button
            color="danger"
            startContent={<LogoutIcon />}
            onPress={logout}
          >
            Logout
          </Button>
        </NavbarItem>
      </NavbarContent>

      
      <NavbarMenu>
        {menuItems.map((item, index) => (
          <NavbarMenuItem key={`${item.label}-${index}`}>
            {item.icon ? (
              <Button
                fullWidth
                color={item.danger ? "danger" : "primary"}
                startContent={item.icon}
                onPress={() => {
                  if (item.label === "Logout") logout();
                  setIsMenuOpen(false);
                }}
                as={Link}
                href={item.href}
              >
                {item.label}
              </Button>
            ) : (
              <Link
                className="w-full"
                color={item.danger ? "danger" : "foreground"}
                href={item.href}
                size="lg"
                onClick={() => setIsMenuOpen(false)}
              >
                {item.label}
              </Link>
            )}
          </NavbarMenuItem>
        ))}
      </NavbarMenu>
    </Navbar>
  );
}
