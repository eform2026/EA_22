import 'package:flutter/material.dart';
import '../services/auth_service.dart';

class ProductFormScreen extends StatefulWidget {
  const ProductFormScreen({super.key});

  @override
  State<ProductFormScreen> createState() => _ProductFormScreenState();
}

class _ProductFormScreenState extends State<ProductFormScreen> {

  final TextEditingController nombreController =
  TextEditingController();

  final TextEditingController descripcionController =
  TextEditingController();

  final TextEditingController precioController =
  TextEditingController();

  final TextEditingController stockController =
  TextEditingController();

  final AuthService authService = AuthService();

  bool loading = false;

  @override
  void initState() {
    super.initState();
    authService.init();
  }

  @override
  void dispose() {
    nombreController.dispose();
    descripcionController.dispose();
    precioController.dispose();
    stockController.dispose();
    super.dispose();
  }

  Future<void> guardarProducto() async {

    if (nombreController.text.isEmpty ||
        descripcionController.text.isEmpty ||
        precioController.text.isEmpty ||
        stockController.text.isEmpty) {
      
      ScaffoldMessenger.of(context)
          .showSnackBar(
        const SnackBar(
          content: Text(
            'Por favor complete todos los campos',
          ),
        ),
      );
      return;
    }

    setState(() {
      loading = true;
    });

    try {
      bool success =
      await authService.createProduct(

        nombre: nombreController.text,

        descripcion:
        descripcionController.text,

        precio:
        double.parse(
            precioController.text),

        stock:
        int.parse(
            stockController.text),
      );

      if (!mounted) return;

      if (success) {

        ScaffoldMessenger.of(context)
            .showSnackBar(
          const SnackBar(
            content: Text(
              'Producto creado correctamente',
            ),
          ),
        );

        Navigator.pop(context);

      } else {

        ScaffoldMessenger.of(context)
            .showSnackBar(
          const SnackBar(
            content: Text(
              'Error creando producto',
            ),
          ),
        );
      }
    } catch (e) {
      if (!mounted) return;

      ScaffoldMessenger.of(context)
          .showSnackBar(
        SnackBar(
          content: Text(
            'Error: $e',
          ),
        ),
      );
    } finally {
      if (mounted) {
        setState(() {
          loading = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        title: const Text(
          "Nuevo Producto",
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [

            TextField(
              controller: nombreController,
              decoration:
              const InputDecoration(
                labelText: "Nombre",
                border:
                OutlineInputBorder(),
              ),
            ),

            const SizedBox(height: 15),

            TextField(
              controller:
              descripcionController,
              decoration:
              const InputDecoration(
                labelText:
                "Descripción",
                border:
                OutlineInputBorder(),
              ),
            ),

            const SizedBox(height: 15),

            TextField(
              controller: precioController,
              keyboardType:
              TextInputType.number,
              decoration:
              const InputDecoration(
                labelText: "Precio",
                border:
                OutlineInputBorder(),
              ),
            ),

            const SizedBox(height: 15),

            TextField(
              controller: stockController,
              keyboardType:
              TextInputType.number,
              decoration:
              const InputDecoration(
                labelText: "Stock",
                border:
                OutlineInputBorder(),
              ),
            ),

            const SizedBox(height: 20),

            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: loading ? null : guardarProducto,
                child: loading 
                  ? const CircularProgressIndicator()
                  : const Text("Guardar"),
              ),
            ),
          ],
        ),
      ),
    );
  }
}