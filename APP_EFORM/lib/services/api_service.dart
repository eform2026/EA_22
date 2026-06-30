import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/product.dart';

class ApiService {
  static const String baseUrl = 'http://localhost:8080';

  // LOGIN
  Future<bool> login(String email, String password) async {
    final response = await http.post(
      Uri.parse('$baseUrl/auth/login'),
      headers: {
        'Content-Type': 'application/json',
      },
      body: jsonEncode({
        'email': email,
        'password': password,
      }),
    );

    if (response.statusCode == 200) {
      return true;
    }

    return false;
  }

  // LISTAR PRODUCTOS
  Future<List<Product>> getProducts() async {
    final response = await http.get(
      Uri.parse('$baseUrl/products'),
    );

    if (response.statusCode == 200) {
      List<dynamic> data = jsonDecode(response.body);

      return data
          .map((item) => Product.fromJson(item))
          .toList();
    }

    throw Exception('Error cargando productos');
  }

  // CREAR PRODUCTO
  Future<bool> createProduct({
    required String nombre,
    required String descripcion,
    required double precio,
    required int stock,
  }) async {
    final response = await http.post(
      Uri.parse('$baseUrl/products'),
      headers: {
        'Content-Type': 'application/json',
      },
      body: jsonEncode({
        'nombre': nombre,
        'descripcion': descripcion,
        'tipoTela': '',
        'imageUrl': '',
        'tallasDisponibles': ['M'],
        'precio': precio,
        'stock': stock,
      }),
    );

    return response.statusCode == 200 ||
        response.statusCode == 201;
  }
}