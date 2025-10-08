export async function verifyToken(baseUrl: string): Promise<boolean> {
  const token = localStorage.getItem("token");
  if (!token) return false;

  try {
    const response = await fetch(`${baseUrl}verifyAuthentication`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
      },
    });

    
    return response.ok;
  } catch (error) {
    console.error("Erro ao verificar token:", error);
    return false;
  }
}
