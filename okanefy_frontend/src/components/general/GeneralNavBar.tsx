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
import { href, useNavigate } from "react-router";

const OkanefyLogo = () => {
  const navigate = useNavigate()
  return (
    <div>
      <Button onPress={() => navigate("/")} className="bg-transparent">
        <img src="src/assets/okanefy_logo.png" className="w-3/6 md:w-4/6 lg:w-3/6" alt="Okanefy Logo"/>
      </Button>
    </div>
  );
};

export default function GeneralNavBar() {
  const [isMenuOpen, setIsMenuOpen] = React.useState(false);

  const menuItems = [
    { label: "Geral", href: "/geral", className: location.pathname === "/geral" ? "font-bold underline text-blue-400" : ""},
    { label: "Categorias", href: "/categorias", className: location.pathname === "/categorias" ? "font-bold underline text-blue-400" : "" },
    { label: "Formas de pagamentos", href: "/formas_pagamento", className: location.pathname === "/formas_pagamento" ? "font-bold underline text-blue-400" : "" },
    { label: "Relatórios", href: "#"},
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
          <Link color="foreground" href="/geral" className={location.pathname === "/geral" ? "font-bold underline text-blue-400" : ""}>
            Geral
          </Link>
        </NavbarItem>
        <NavbarItem>
          <Link color="foreground" href="/categorias" className={location.pathname === "/categorias" ? "font-bold underline text-blue-400" : ""}>
            Categorias
          </Link>
        </NavbarItem>
        <NavbarItem>
          <Link color="foreground" href="/formas_pagamento" className={location.pathname === "/formas_pagamento" ? "font-bold underline text-blue-400" : ""}>
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
                className={`w-full ${item.className}`}
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
