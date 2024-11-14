package com.haw.mobiledeviceprogramming.presentation.utils

import com.app.mobiledeviceprogramming.R

data class Medicine(
    val name: String,
    val description: String,
    val imageRes: Int
)

val sampleMedicine = listOf(
    Medicine("Paracetamol", "Pain reliever and fever reducer", R.drawable.img_pills),
    Medicine("Ibuprofen", "Anti-inflammatory painkiller", R.drawable.img_pills),
    Medicine("Amoxicillin", "Antibiotic used to treat bacterial infections", R.drawable.img_pills),
    Medicine("Loratadine", "Antihistamine used for allergy relief", R.drawable.img_pills),
    Medicine("Metformin", "Medication for type 2 diabetes", R.drawable.img_pills),
    Medicine("Aspirin", "Pain reliever and anti-inflammatory", R.drawable.img_pills),
    Medicine("Atorvastatin", "Cholesterol-lowering medication", R.drawable.img_pills),
    Medicine("Omeprazole", "Acid reflux and heartburn treatment", R.drawable.img_pills),
    Medicine("Levothyroxine", "Thyroid hormone replacement", R.drawable.img_pills),
    Medicine("Simvastatin", "Medication for high cholesterol", R.drawable.img_pills),
    Medicine("Amlodipine", "Medication for high blood pressure", R.drawable.img_pills),
    Medicine("Ciprofloxacin", "Antibiotic for bacterial infections", R.drawable.img_pills),
    Medicine("Clopidogrel", "Blood thinner to prevent clots", R.drawable.img_pills),
    Medicine("Losartan", "Medication for hypertension", R.drawable.img_pills),
    Medicine("Prednisone", "Steroid for inflammation and allergies", R.drawable.img_pills),
    Medicine("Alprazolam", "Medication for anxiety", R.drawable.img_pills),
    Medicine("Azithromycin", "Antibiotic for bacterial infections", R.drawable.img_pills),
    Medicine("Metoprolol", "Beta-blocker for high blood pressure", R.drawable.img_pills),
    Medicine("Diclofenac", "Pain reliever and anti-inflammatory", R.drawable.img_pills),
    Medicine("Gabapentin", "Nerve pain and seizure treatment", R.drawable.img_pills),
    Medicine("Hydrochlorothiazide", "Diuretic for hypertension", R.drawable.img_pills),
    Medicine("Sertraline", "Antidepressant medication", R.drawable.img_pills),
    Medicine("Doxycycline", "Antibiotic for bacterial infections", R.drawable.img_pills),
    Medicine("Tamsulosin", "Treatment for urinary issues in men", R.drawable.img_pills),
    Medicine("Cetirizine", "Antihistamine for allergy relief", R.drawable.img_pills),
    Medicine("Enalapril", "Medication for high blood pressure", R.drawable.img_pills),
    Medicine("Ranitidine", "Acid reducer for heartburn", R.drawable.img_pills),
    Medicine("Montelukast", "Asthma and allergy medication", R.drawable.img_pills),
    Medicine("Fluconazole", "Antifungal treatment", R.drawable.img_pills),
    Medicine("Meloxicam", "Pain reliever for arthritis", R.drawable.img_pills),
    Medicine("Lisinopril", "ACE inhibitor for hypertension", R.drawable.img_pills),
    Medicine("Furosemide", "Diuretic for fluid retention", R.drawable.img_pills),
    Medicine("Clindamycin", "Antibiotic for bacterial infections", R.drawable.img_pills),
    Medicine("Benzonatate", "Cough suppressant", R.drawable.img_pills),
    Medicine("Spironolactone", "Diuretic and anti-androgen", R.drawable.img_pills),
    Medicine("Naproxen", "Anti-inflammatory pain reliever", R.drawable.img_pills),
    Medicine("Warfarin", "Blood thinner for clot prevention", R.drawable.img_pills),
    Medicine("Tramadol", "Pain reliever for moderate to severe pain", R.drawable.img_pills),
    Medicine("Propranolol", "Beta-blocker for blood pressure and anxiety", R.drawable.img_pills),
    Medicine("Zolpidem", "Medication for insomnia", R.drawable.img_pills),
    Medicine("Hydrocodone", "Pain reliever for severe pain", R.drawable.img_pills),
    Medicine("Mirtazapine", "Antidepressant medication", R.drawable.img_pills),
    Medicine("Esomeprazole", "Treatment for acid reflux", R.drawable.img_pills),
    Medicine("Allopurinol", "Gout prevention medication", R.drawable.img_pills),
    Medicine("Bupropion", "Antidepressant and smoking cessation aid", R.drawable.img_pills),
    Medicine("Rosuvastatin", "Cholesterol-lowering medication", R.drawable.img_pills),
    Medicine("Famotidine", "Acid reducer for heartburn", R.drawable.img_pills),
    Medicine("Cyclobenzaprine", "Muscle relaxant", R.drawable.img_pills),
    Medicine("Ondansetron", "Prevents nausea and vomiting", R.drawable.img_pills),
    Medicine("Oxycodone", "Pain reliever for severe pain", R.drawable.img_pills),
    Medicine("Venlafaxine", "Antidepressant medication", R.drawable.img_pills),
    Medicine("Finasteride", "Medication for prostate health", R.drawable.img_pills)
)

fun getMedicine(): Medicine {
    return sampleMedicine.random()
}