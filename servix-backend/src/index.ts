import express, { Request, Response } from "express";
import cors from "cors";

const app = express();
app.use(express.json());
app.use(cors());

const PORT = process.env.PORT || 3000;

interface Trabajo {
  id: string;
  titulo: string;
  categoria: string;
  zona: string;
}

let trabajos: Trabajo[] = [
  {
    id: "1",
    titulo: "Instalación de tubería",
    categoria: "Plomería",
    zona: "Usaquén",
  },
];

app.get("/api/v1/trabajos", (req: Request, res: Response) => {
  res.status(200).json(trabajos);
});

app.get("/api/v1/trabajos/:id", (req: Request, res: Response) => {
  const { id } = req.params;
  const trabajo = trabajos.find((t) => t.id === id);

  if (!trabajo) {
    return res.status(404).json({ error: "Trabajo no encontrado" });
  }

  return res.status(200).json(trabajo);
});

app.post("/api/v1/trabajos", (req: Request, res: Response) => {
  const { titulo, categoria, zona } = req.body;

  if (!titulo || !categoria || !zona) {
    return res
      .status(400)
      .json({ error: "Faltan campos obligatorios (titulo, categoria, zona)" });
  }

  const nuevoTrabajo: Trabajo = {
    id: Date.now().toString(),
    titulo,
    categoria,
    zona,
  };

  trabajos.push(nuevoTrabajo);
  return res.status(201).json(nuevoTrabajo);
});

app.patch("/api/v1/trabajos/:id", (req: Request, res: Response) => {
  const { id } = req.params;
  const { titulo, categoria, zona } = req.body;

  const trabajo = trabajos.find((t) => t.id === id);
  if (!trabajo) {
    return res
      .status(404)
      .json({ error: "Trabajo no encontrado para actualizar" });
  }

  if (titulo) trabajo.titulo = titulo;
  if (categoria) trabajo.categoria = categoria;
  if (zona) trabajo.zona = zona;

  return res.status(200).json({
    mensaje: "Trabajo actualizado con éxito",
    trabajo,
  });
});

app.delete("/api/v1/trabajos/:id", (req: Request, res: Response) => {
  const { id } = req.params;
  const index = trabajos.findIndex((t) => t.id === id);

  if (index === -1) {
    return res
      .status(404)
      .json({ error: "Trabajo no encontrado para eliminar" });
  }

  trabajos.splice(index, 1);
  return res.status(200).json({ mensaje: "Trabajo eliminado correctamente" });
});

app.listen(PORT, () => {
  console.log(`Servidor corriendo en http://localhost:${PORT}`);
});
