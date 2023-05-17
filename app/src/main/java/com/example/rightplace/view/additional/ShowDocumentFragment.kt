package com.example.rightplace


import android.R
import android.content.Intent
import android.graphics.*
import android.graphics.Typeface.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.navigation.fragment.navArgs
import com.example.rightplace.databinding.FragmentShowDocumentBinding
import com.example.rightplace.model.Document
import com.example.rightplace.model.Space
import java.io.File
import java.io.FileOutputStream


class ShowDocumentFragment: BaseFragment() {
    private var _binding: FragmentShowDocumentBinding? = null
    private val binding get() = _binding!!

    var pageHeight = 600
    var pageWidth = 500

    // creating a bitmap variable
    // for storing our images
    lateinit var bmp: Bitmap
    lateinit var scaledbmp: Bitmap

    private val safeArgs : ShowDocumentFragmentArgs by navArgs()
    private val selectedDocument : Document? by lazy{
        sharedViewModel.allDocumentsLiveData.value?.find {
            it.id == safeArgs.documentId
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.supportActionBar?.title="Podgląd i edycja dokumentu"

        binding.saveButton.setOnClickListener {
            saveDocumentToDatabase()
        }
        sharedViewModel.transactionCompleteLiveData.observe(viewLifecycleOwner){complete ->
            if(complete){
                binding.pdfButton.visibility = View.GONE
                binding.pdfShowButton.visibility = View.GONE
                    return@observe
            }
        }
        binding.nameEditText.requestFocus()
        spaceViewModel.spaceLiveData.observe(viewLifecycleOwner){

        }


        val spinner: Spinner = binding.typesSpinner
        documentTypeViewModel.documentTypeLiveData.observe(viewLifecycleOwner){ documentTypeList ->
            val tab: kotlin.collections.ArrayList <String> = ArrayList<String>()
            val ids: kotlin.collections.ArrayList <String> = ArrayList<String>()
            documentTypeList.forEach {
                tab.add(it.Name.toString())
                ids.add(it.id)
            }
            selectedDocument?.let {document ->
                val space : Space? by lazy{
                    spaceViewModel.spaceLiveData.value?.find{
                        it.id==document.RoomId
                    }

                }
                binding.spaceField.text = space?.Name
                binding.typeIdField.text = document.TypeId
                binding.typesSpinner.visibility=View.GONE
                binding.typeField.text = tab[ids.indexOf(document.TypeId)]
                binding.nameEditText.setText(document.Name)
                binding.descriptionEditText.setText(document.Description)



                val qrCodeFile = File("/storage/self/primary/DCIM/QR/"+document.id+"_qrcode.png")
                val myBitmap = BitmapFactory.decodeFile(qrCodeFile.absolutePath)


                binding.imageView.setImageBitmap(myBitmap)

                myBitmap?.let{
                    binding.pdfButton.visibility=View.VISIBLE

                    binding.pdfButton.setOnClickListener {
                        val file = generatePDF(myBitmap,document.Name.toString())
                        binding.pdfShowButton.visibility=View.VISIBLE
                        binding.pdfShowButton.setOnClickListener {
                            openFile(file)
                        }
                    }

                }




            }
            binding.changeButton.setOnClickListener {
                binding.typeTextField.visibility = View.GONE
                binding.typeField.visibility = View.GONE
                binding.typesSpinner.visibility=View.VISIBLE

            }
            val adapter : ArrayAdapter<String> = ArrayAdapter(
                requireActivity(),
                R.layout.simple_spinner_item,tab)

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter

            binding.nameEditText.requestFocus()

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {



                    binding.saveButton.setOnClickListener {
                        saveDocumentToDatabase(ids[p2])
                        binding.typesSpinner.visibility=View.GONE
                        binding.typeField.text = tab[p2]
                        binding.typeTextField.visibility = View.VISIBLE
                        binding.typeField.visibility = View.VISIBLE

                    }

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }

    }
    fun openFile(file: File) {

        // Get URI and MIME type of file
        val uri: Uri = FileProvider.getUriForFile(requireActivity(),  BuildConfig.APPLICATION_ID + ".provider", file)
        val mime: String? = requireActivity().contentResolver.getType(uri)

        // Open file with user selected app
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(uri, mime)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    private fun saveDocumentToDatabase(id :String = ""){

        val documentName = binding.nameEditText.text.toString().trim()
        if (documentName.isEmpty()){
            binding.nameTextField.error = "Pole wymagane"
            return
        }
        binding.nameTextField.error = null
        val documentDescription = binding.descriptionEditText.text.toString().trim()

        val document = selectedDocument!!.copy(
            Name = documentName,
            Description = documentDescription
        )
        if (id == ""){
            document.TypeId=binding.typeIdField.text.toString()
        }
        else{
            document.TypeId=id
        }
        sharedViewModel.updateDocument(document)
        Toast.makeText(requireActivity(), "Zaktualizowano pomyślnie", Toast.LENGTH_SHORT).show()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun generatePDF(bitmap: Bitmap, documentName: String ): File {
        var pdfDocument: PdfDocument = PdfDocument()

        var paint: Paint = Paint()
        var title: Paint = Paint()

        var myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        var canvas: Canvas = myPage.canvas

        canvas.drawBitmap(bitmap, 32F, 40F, paint)

        title.textSize = 30F

        title.textAlign = Paint.Align.CENTER
        canvas.drawText("Nazwa dokumentu:", 250F, 510F, title)
        canvas.drawText(documentName, 250F, 560F, title)

        pdfDocument.finishPage(myPage)

        val file: File = File("/storage/self/primary/Documents", "$documentName.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(file))

            Toast.makeText(requireActivity(), "PDF file generated..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(requireActivity(), "Fail to generate PDF file..", Toast.LENGTH_SHORT)
                .show()
        }
        pdfDocument.close()
        return file
    }

}