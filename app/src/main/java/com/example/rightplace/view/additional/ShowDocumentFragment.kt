package com.example.rightplace.view.additional


import com.example.rightplace.R
import android.content.Intent
import android.graphics.*
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
import com.example.rightplace.view.BaseFragment
import com.example.rightplace.BuildConfig
import com.example.rightplace.databinding.FragmentShowDocumentBinding
import com.example.rightplace.model.Document
import com.example.rightplace.model.Space
import java.io.File
import java.io.FileOutputStream


class ShowDocumentFragment: BaseFragment() {
    private var _binding: FragmentShowDocumentBinding? = null
    private val binding get() = _binding!!

    private var pageHeight = 600
    private var pageWidth = 500


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
    ): View {
        _binding = FragmentShowDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.supportActionBar?.title=getString(R.string.show_edit)
        if(selectedDocument==null){
            Toast.makeText(requireActivity(), getString(R.string.doc_not_found), Toast.LENGTH_LONG).show()
            navigateUp()
        }
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
            val tab: ArrayList <String> = ArrayList()
            val ids: ArrayList <String> = ArrayList()
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
                android.R.layout.simple_spinner_item,tab)

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
    private fun openFile(file: File) {


        val uri: Uri = FileProvider.getUriForFile(requireActivity(),  BuildConfig.APPLICATION_ID + ".provider", file)
        val mime: String? = requireActivity().contentResolver.getType(uri)


        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(uri, mime)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    private fun saveDocumentToDatabase(id :String = ""){

        val documentName = binding.nameEditText.text.toString().trim()
        if (documentName.isEmpty()){
            binding.nameTextField.error = getString(R.string.required)
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
        Toast.makeText(requireActivity(), getString(R.string.update_success), Toast.LENGTH_SHORT).show()


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generatePDF(bitmap: Bitmap, documentName: String ): File {
        val pdfDocument = PdfDocument()

        val paint = Paint()
        val title = Paint()

        val myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        val myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        val canvas: Canvas = myPage.canvas

        canvas.drawBitmap(bitmap, 32F, 40F, paint)

        title.textSize = 30F

        title.textAlign = Paint.Align.CENTER
        canvas.drawText(getString(R.string.doc_name), 250F, 510F, title)
        canvas.drawText(documentName, 250F, 560F, title)

        pdfDocument.finishPage(myPage)

        val file = File("/storage/self/primary/Documents", "$documentName.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(file))

            Toast.makeText(requireActivity(), getString(R.string.pdf_ready), Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(requireActivity(), getString(R.string.pdf_error), Toast.LENGTH_SHORT)
                .show()
        }
        pdfDocument.close()
        return file
    }

}